package com.redis.redis;

import java.util.HashMap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import redis.clients.jedis.Jedis;

@SpringBootApplication
public class RedisApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(RedisApplication.class, args);
	}
@Override
public void run(String... args) throws Exception {
	Jedis jedis = new Jedis();
	System.out.println("Jedis Object is created \n");
	
	System.out.println("Response from Redis Server "+jedis.ping());
	
	jedis.set("gc:user1", "vipin patel");
	
	System.out.println("jedis user " +jedis.get("gc:user1"));

	jedis.hset("gc:user:profile", "Name", "Vipin Patel");
	jedis.hset("gc:user:profile", "firstName", "Vipin");
	jedis.hset("gc:user:profile", "lastName", "Patel");
	jedis.hset("gc:user:profile", "email", "vipin@gc.com");
	jedis.hset("gc:user:profile", "type", "employeer");
	System.out.println(jedis.hgetAll("gc:user:profile"));
	
	HashMap<String,String> hashmap =(HashMap<String, String>) jedis.hgetAll("gc:user:profile");
	
	System.out.println(hashmap);
	
	String name = jedis.hget("gc:user:profile", "Name");
	System.out.println("name " + name);
	
}
}
