package com.example.demo.service.impl;

import com.example.demo.model.Address;
import com.example.demo.repository.AddressRepository;
import com.example.demo.service.abs.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository ar;

    @Override
    public List<Address> getAllAddresses() {
        return ar.findAll();
    }

    @Override
    public Address getAddressById(int id) {
        return ar.getReferenceById(id);
    }

    @Override
    public boolean saveOrUpdateAddress(Address address) {
        try {
            ar.save(address);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean createAddress(Address address) {
        try {
            ar.save(address);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteAddress(int id) {
        try {
            ar.deleteById(id);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }
}
