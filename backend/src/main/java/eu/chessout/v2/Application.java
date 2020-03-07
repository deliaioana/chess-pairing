package eu.chessout.v2;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

            initializeFirebase();



        };
    }

    private void initializeFirebase() throws IOException {

        FileInputStream serviceAccount = getCredentialsInputStream();

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://my-project-id.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);
    }

    private FileInputStream getCredentialsInputStream() throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resouce = classLoader.getResource("firebase-chessout-v2.json");
        File file = new File(resouce.getFile());
        FileInputStream fileInputStream = new FileInputStream(file);
        return fileInputStream;
    }
}
