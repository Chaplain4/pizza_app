package com.example.demo.service.impl;

import com.example.demo.model.Feedback;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.service.abs.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackRepository fr;

    @Override
    public List<Feedback> getAllFeedbacks() {
        return fr.findAll();
    }

    @Override
    public Feedback getFeedbackById(int id) {
        return fr.getReferenceById(id);
    }

    @Override
    public boolean saveOrUpdateFeedback(Feedback feedback) {
        try {
            fr.save(feedback);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean createFeedback(Feedback feedback) {
        try {
            fr.save(feedback);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean deleteFeedback(int id) {
        try {
            fr.deleteById(id);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }
}
