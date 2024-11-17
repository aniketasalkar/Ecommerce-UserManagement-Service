package com.example.usermanagementservice.services;

import com.example.usermanagementservice.exceptions.MaximumAddressesReachedException;
import com.example.usermanagementservice.exceptions.UserNotFoundException;
import com.example.usermanagementservice.models.Address;
import com.example.usermanagementservice.models.User;
import com.example.usermanagementservice.repository.AddressRepository;
import com.example.usermanagementservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AddressService implements IAddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public List<Address> addAddress(Address address, Long userId) {

//        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        User user = userRepository.findUserById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        System.out.println("Addresses: " + user.getAddresses());
        System.out.println("previousSet AddressCount: " + user.getAddresses().size());
        List<Address> addressList = user.getAddresses().stream().collect(Collectors.toList());
        int userAddressCount = addressList.size();

        if (addressList.isEmpty()) {
            addressList = new ArrayList<>();
        } else if (userAddressCount == 10) {
            throw new MaximumAddressesReachedException("Maximum addresses reached. Only 10 addresses are allowed.");
        }

        for (Address addressItem : addressList) {
            addressItem.setDeliveryAddress(false);
        }

        Date currentDate = new Date();
        address.setCreatedAt(currentDate);
        address.setUpdatedAt(currentDate);
        address.setDeliveryAddress(true);

        addressList.add(address);

        user.setAddresses(addressList.stream().collect(Collectors.toSet()));
//        user.setAddresses(addressList);

        System.out.println("Previous Count: " + userAddressCount);
        System.out.println("Newer Count: " + user.getAddresses().size());

        if (user.getAddresses().size() != userAddressCount) {
            addressRepository.save(address);
            userRepository.save(user);
        }

        return userRepository.findById(userId).get().getAddresses().stream().toList();
    }
}
