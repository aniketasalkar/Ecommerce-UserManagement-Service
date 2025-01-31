package com.example.usermanagementservice.services;

import com.example.usermanagementservice.exceptions.*;
import com.example.usermanagementservice.models.Address;
import com.example.usermanagementservice.models.User;
import com.example.usermanagementservice.models.UserAddress;
import com.example.usermanagementservice.repository.AddressRepository;
import com.example.usermanagementservice.repository.UserAddressRepository;
import com.example.usermanagementservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AddressService implements IAddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Override
    @Transactional
    public List<Address> addAddress(Address address, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Check max addresses
        if (user.getUserAddresses().size() >= 10) {
            throw new MaximumAddressesReachedException("Maximum addresses reached (10 allowed).");
        }

        Date now = new Date();

        Optional<Address> existingAddress = addressRepository.findByNameAndStreetAndCityAndStateAndZipAndCountry(
                address.getName(),
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getZip(),
                address.getCountry()
        );

        Address savedAddress;
        if (existingAddress.isPresent()) {
            // Reuse existing address
            savedAddress = existingAddress.get();
            if (userAddressRepository.existsByUserAndAddress(user, savedAddress)) {
                throw new DuplicateAddressException("Address already added for this user.");
            }
        } else {
            // Set timestamps and save new address
            address.setCreatedAt(now);
            address.setUpdatedAt(now);
            savedAddress = addressRepository.save(address);
        }

        // Create UserAddress relationship
        UserAddress userAddress = new UserAddress();
        userAddress.setUser(user);

        // Initialize deliveryAddress
        userAddress.setDeliveryAddress(false); // Default to false

        // If this is the user's first address, set it as delivery
        if (user.getUserAddresses().isEmpty()) {
            userAddress.setDeliveryAddress(true);
        }

        userAddress.setAddress(savedAddress);

        // Add to user's addresses
        Set<UserAddress> userAddresses = user.getUserAddresses();
        userAddresses.add(userAddress);
        user.setUserAddresses(userAddresses);
        User savedUser = userRepository.save(user);

        return savedUser.getUserAddresses().stream()
                .map(UserAddress::getAddress)
                .toList();
    }

    @Override
    public Address getDefaultAddress(Long userId) {
        User user = userRepository.findUserById(userId).
                orElseThrow(() -> new UserNotFoundException("User not found"));

        Address address = user.getUserAddresses().stream()
                .filter(userAddress -> userAddress.getDeliveryAddress() == true)
                .map(userAddress -> userAddress.getAddress())
                .findFirst()
                .orElseThrow(() -> new NoDefaultAddressFoundException("Default address not found"));

        return address;
    }

    @Transactional
    @Override
    public Address updateDefaultAddress(Long addressId, Long userId) {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Address address = addressRepository.findAddressesById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Address not found"));

        UserAddress userAddress = userAddressRepository.findByUserAndDeliveryAddressIsTrue(user)
                .orElseThrow(() -> new NoDefaultAddressFoundException("Default address not found"));

        userAddress.setDeliveryAddress(false);

        UserAddress newDefaultAddress = userAddressRepository.findByUserAndAddress(user, address)
                .orElseThrow(() -> new InvalidDataException("Address does not belong to user"));

        newDefaultAddress.setDeliveryAddress(true);
        userAddressRepository.save(userAddress);
        userAddressRepository.save(newDefaultAddress);

        return address;
    }

    @Override
    public List<Address> getAddressesByUserId(Long userId) {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));


        return user.getUserAddresses().stream()
                .filter(userAddress -> userAddress.getUser().equals(user))
                .map(UserAddress::getAddress)
                .collect(Collectors.toList());
    }
}
