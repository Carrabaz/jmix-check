package com.company.training.app;

import com.company.training.entity.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PostService {

    public Post[] fetchPosts() {
        RestTemplate rest = new RestTemplate();
        Post[] p = rest.getForObject("http://jsonplaceholder.typicode.com/posts", Post[].class);
        //rest.getForEntity("https://jsonplaceholder.typicode.com/posts", Post.class);
        return p;
    }
}