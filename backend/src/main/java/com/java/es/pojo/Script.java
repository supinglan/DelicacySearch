package com.java.es.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Script {
     String pict_url;
     String html_url;
     String title;
     String Abstract;
     ArrayList<String> ingredient;
     ArrayList<String> steps;
     String origin;
}
