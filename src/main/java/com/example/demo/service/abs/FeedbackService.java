package com.example.demo.service.abs;


import com.example.demo.model.Feedback;

import java.util.List;

public interface FeedbackService {
    List<Feedback> getAllFeedbacks();
    Feedback getFeedbackById(int id);
    boolean saveOrUpdateFeedback(Feedback feedback);
    boolean createFeedback(Feedback feedback);
    boolean deleteFeedback(int id);
}
