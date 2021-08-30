package com.testcase.conference;

import com.testcase.conference.Repository.RoomRepository;
import com.testcase.conference.Repository.TalkRepository;
import com.testcase.conference.Repository.UserRepository;
import com.testcase.conference.entity.Room;
import com.testcase.conference.entity.Talk;
import com.testcase.conference.entity.TalkSchedule;
import com.testcase.conference.entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@Import({SwaggerConfig.class})
public class ConferenceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConferenceApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Bean
//	CommandLineRunner initRecords(
//			UserRepository userRepository,
//			RoomRepository roomRepository,
//			TalkRepository talkRepository,
//			PasswordEncoder passwordEncoder
//	) {
//		return args -> {
//			User u1 = new User("Keker", passwordEncoder.encode("password"), User.Roles.ADMIN);
//			User u2 = new User("Talker1", passwordEncoder.encode("password"), User.Roles.SPEAKER);
//			User u3 = new User("Talker2", passwordEncoder.encode("password"), User.Roles.SPEAKER);
//			User u4 = new User("lis1", passwordEncoder.encode("password"), User.Roles.LISTENER);
//			User u5 = new User("lis2", passwordEncoder.encode("password"), User.Roles.LISTENER);
//			User u6 = new User("lis3", passwordEncoder.encode("password"), User.Roles.LISTENER);
//			User u7 = new User("lis4", passwordEncoder.encode("password"), User.Roles.LISTENER);
//
//			userRepository.saveAll(Arrays.asList(
//					u1, u2, u3, u4, u5, u6, u7
//			));
//
//			List<User> userList = userRepository.findAll();
//
//
//			Talk t1 = new Talk("Meme basics", userList.get(1));
//			t1.addSpeakers(userList.get(2));
//			Talk t2 = new Talk("Meme advanced", userList.get(2));
//
//			userList.remove(0);
//			userList.remove(1);
//			userList.remove(2);
//
//			t1.setUsers(userList);
//
//			System.out.println(t1);
//
//			t2.setUsers(userList);
//			talkRepository.saveAll(Arrays.asList(
//					t1, t2
//			));
//
//			roomRepository.saveAll(Arrays.asList(
//					new Room(1), new Room(2 ), new Room(3), new Room(4)
//			));
//			List<Room> roomList = roomRepository.findAll();
//
//			TalkSchedule ts1 = new TalkSchedule(roomList.get(0), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
//			ts1.setTalk(t1);
//			TalkSchedule ts2 = new TalkSchedule(roomList.get(1), LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.MINUTES));
//			ts2.setTalk(t2);
//
//			talkRepository.saveAll(Arrays.asList(
//					t1, t2
//			));
//
//		};
//	}


}