package com.redis.redis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@SpringBootApplication
public class RedisApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(RedisApplication.class, args);
	}
@Override
public void run(String... args) throws Exception {
	Jedis jedis = new Jedis();
	System.out.println("Jedis Objecjom Redis Server "+jedis.ping());
	
	jedis.set("gcn:user1", "vipin patel");
	
	System.out.println("jedis user " +jedis.get("gc:user1"));

	jedis.hset("gc:user:profile", "Name", "Vipin Patel");
	jedis.hset("gc:user:profile", "firstName", "Vipin");
	jedis.hset("gc:user:profile", "lastName", "Patel");
	jedis.hset("gc:user:profile", "email", "vipin@gc.com");
	jedis.hset("gc:user:profile", "type", "employeer");
	System.out.println(jedis.hgetAll("gc:user:profile"));
	
	HashMap<String,String> hashmap =(HashMap<String, String>) jedis.hgetAll("gc:user:profile");
	
	System.out.println(hashmap);
	
	jedis.sadd("planet", "pluto");
//	Set<String> Dplanet= new HashSet<String>();
//	Dplanet.add("mars");
	jedis.sadd("Dplanet", "mars");
	
	System.out.println(jedis.smove("planet", "Dplanet", "pluto"));
	System.out.println("Card " +jedis.scard("Dplanet"));
	
	
	jedis.close();
	String name = jedis.hget("gc:user:profile", "Name");
	System.out.println("name " + name);
	
	Jedis jedis2 = new Jedis();
	String [] familyMember = {"Vipin Pate","Nitin Patel","Rachit patel","Rachit patel"};
	
	jedis2.lpush("familyMember",familyMember);
	System.out.println(jedis2.lrange("familyMember",0,-1));
	//System.out.println(jedis2.lpop("familyMember"));
	System.out.println(jedis2.lrange("familyMember",0,-1));
//	System.out.println(jedis2.lpop("familyMember"));
	
	Map<String, String> familyMemberMap = new HashMap<String, String>();
	familyMemberMap.put("Name", "Vipin");
	familyMemberMap.put("age","25");
	familyMemberMap.put("city","jabalpur");
	
	jedis2.hset("gc:employee1", familyMemberMap);
	System.out.println(jedis2.hgetAll("gc:employee1"));
	System.out.println(jedis2.hget("gc:employee1","Name"));
	
	jedis2.sadd("familyMem", familyMember);
	System.out.println(jedis.scard("familyMem"));
	System.out.println(jedis2.smembers("familyMem"));
	jedis2.close();
	
//	Set<String> planet= new HashSet<String>();
//	planet.add("pluto");
	JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "localhost", 6379);

	
    
	for (int i=0; i < 10; i++) {
	   try(Jedis jedis1= jedisPool.getResource()){
	   jedis1.set(String.valueOf(i), "0");
	   System.out.println(jedis1.get(String.valueOf(i)));
	}
	}
	
	try (Jedis jedis3 = jedisPool.getResource()) {
	    Long setSize = jedis3.scard("messages");
	    System.out.println(setSize);
	    System.out.println("Size: " + String.valueOf(setSize));
	    jedis.srem("messages");
	    //jedis3.close();
	}
}
}
