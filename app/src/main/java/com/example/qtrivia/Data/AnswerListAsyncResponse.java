package com.example.qtrivia.Data;

import com.example.qtrivia.Model.Question;

import java.util.ArrayList;

public interface AnswerListAsyncResponse {
    void processFinished(ArrayList<Question> questions);
}
