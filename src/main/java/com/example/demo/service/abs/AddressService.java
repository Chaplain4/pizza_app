package com.example.demo.service.abs;

import com.example.demo.model.Address;

import java.util.List;

public interface AddressService {
    List<Address> getAllAddresses();
    Address getAddressById(int id);
    boolean saveOrUpdateAddress(Address address);
    boolean createAddress(Address address);
}
