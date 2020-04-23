package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.exception.ResourceNotFoundException;
import com.bookshop.bookshop.model.Topic;
import com.bookshop.bookshop.model.User;
import com.bookshop.bookshop.payload.LoginRequest;
import com.bookshop.bookshop.payload.SignUpRequest;
import com.bookshop.bookshop.payload.TopicRequest;
import com.bookshop.bookshop.repository.TopicRepository;
import com.bookshop.bookshop.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest()
public class TopicControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));


    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mvc;

    @Before
    public void init() {
        userRepository.deleteAll();
        topicRepository.deleteAll();
        this.mvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(springSecurityFilterChain).build();
    }

    @Test
    public void givenNoToken_whenGetSecureRequest_thenUnauthrized() throws Exception {
        mvc.perform(post("/api/topics")
                .param("title", "Topic Name")
                .param("description", "Topic Description"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }


    @Test
    public void should_return_is_Ok_status() throws Exception {
        String requestJson = obtaingAccessToken("wincent123", "Antek Kurek", "wincent123@gmail.com", "antek124");

        ResultActions result = mvc.perform(post("/api/auth/signup")
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson)).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void should_return_is_Login_status() throws Exception {
        String ReqisterRequest = obtaingAccessToken("wincent123", "Antek Kurek", "wincent123@gmail.com", "antek124");

        ResultActions result = mvc.perform(post("/api/auth/signup")
                .contentType(APPLICATION_JSON_UTF8)
                .content(ReqisterRequest)).andExpect(MockMvcResultMatchers.status().isCreated());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("wincent123");
        loginRequest.setPassword("antek124");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String LoginRequest = ow.writeValueAsString(loginRequest);

        result = mvc.perform(post("/api/auth/signin")
                .contentType(APPLICATION_JSON_UTF8).content(LoginRequest)).andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void should_return_topic_is_Created() throws Exception {
        String accessToken = returnAccessToken();

        TopicRequest topicRequest = new TopicRequest();
        topicRequest.setTitle("The First Topic");
        topicRequest.setDescription("The Description of Topic");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String request = ow.writeValueAsString(topicRequest);

        ResultActions result = mvc.perform(post("/api/topics")
                .contentType(APPLICATION_JSON_UTF8).content(request).header("Authorization", "Bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void should_return_topics_return_status_Ok() throws Exception {
        String accessToken = returnAccessToken();

        TopicRequest topicRequest = new TopicRequest();
        topicRequest.setTitle("The Twice Topic");
        topicRequest.setDescription("The Description of Twice Topic");


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String request = ow.writeValueAsString(topicRequest);

        ResultActions result = mvc.perform(post("/api/topics")
                .contentType(APPLICATION_JSON_UTF8).content(request).header("Authorization", "Bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        result = mvc.perform(get("/api/topics")
                .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk());

        String topics = result.andReturn().getResponse().getContentAsString();
        System.out.println(topics);
    }

    @Test
    public void should_return_topic_by_id_return_ok() throws Exception {
        String accessToken = returnAccessToken();

        TopicRequest topicRequest = new TopicRequest();
        topicRequest.setTitle("The Twice Topic");
        topicRequest.setDescription("The Description of Twice Topic");


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String request = ow.writeValueAsString(topicRequest);

        ResultActions result = mvc.perform(post("/api/topics")
                .contentType(APPLICATION_JSON_UTF8).content(request).header("Authorization", "Bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        result = mvc.perform(get("/api/topics")
                .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk());

        String topics = result.andReturn().getResponse().getContentAsString();

        List<Topic> topicList = topicRepository.findAll();

        Long topicId = topicList.get(0).getId();

        result = mvc.perform(get("/api/topics/" + topicId).header("Authorization", "Bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    private String returnAccessToken() throws Exception {
        String ReqisterRequest = obtaingAccessToken("wincent123", "Antek Kurek", "wincent123@gmail.com", "antek124");

        ResultActions result = mvc.perform(post("/api/auth/signup")
                .contentType(APPLICATION_JSON_UTF8)
                .content(ReqisterRequest)).andExpect(MockMvcResultMatchers.status().isCreated());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("wincent123");
        loginRequest.setPassword("antek124");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String LoginRequest = ow.writeValueAsString(loginRequest);

        result = mvc.perform(post("/api/auth/signin")
                .contentType(APPLICATION_JSON_UTF8).content(LoginRequest)).andExpect(MockMvcResultMatchers.status().isOk());

        String resultString = result.andReturn().getResponse().getContentAsString();

        JsonParser springParser = JsonParserFactory.getJsonParser();
        Map<String, Object> map = springParser.parseMap(resultString);

        String val = (String) map.get("accessToken");

        return val;

    }

    private String obtaingAccessToken(String username, String name, String email, String password) throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername(username);
        signUpRequest.setName(name);
        signUpRequest.setEmail(email);
        signUpRequest.setPassword(password);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson= ow.writeValueAsString(signUpRequest);

        return requestJson;


    }


}

