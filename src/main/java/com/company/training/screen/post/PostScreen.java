package com.company.training.screen.post;

import com.company.training.app.PostService;
import com.company.training.entity.Post;
import io.jmix.core.LoadContext;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@UiController("t_PostScreen")
@UiDescriptor("post-screen.xml")
public class PostScreen extends Screen {

    @Autowired
    private PostService postService;

    @Install(to = "postsDl", target = Target.DATA_LOADER)
    private List<Post> postsDlLoadDelegate(LoadContext<Post> loadContext) {
        return Arrays.asList(postService.fetchPosts());
    }
}