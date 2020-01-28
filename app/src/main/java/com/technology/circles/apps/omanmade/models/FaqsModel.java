package com.technology.circles.apps.omanmade.models;

import java.io.Serializable;
import java.util.List;

public class FaqsModel implements Serializable {
private List<Faqs> faqs;

    public List<Faqs> getFaqs() {
        return faqs;
    }

    public class Faqs implements Serializable
    {
        private int id;

            private String question;
            private String answer;

        public int getId() {
            return id;
        }

        public String getQuestion() {
            return question;
        }

        public String getAnswer() {
            return answer;
        }
    }
}
