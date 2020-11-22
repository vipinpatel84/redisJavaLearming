package com.redis.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;
import sun.jvm.hotspot.gc.z.ZCollectedHeap;

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

	
    
//	for (int i=0; i < 10; i++) {
//	   try(Jedis jedis1= jedisPool.getResource()){
//	   jedis1.set(String.valueOf(i), "0");
//	   System.out.println(jedis1.get(String.valueOf(i)));
//	}
//	}
	
//	insert(0, "25.0");
//	insert(1, "26.1");
//	insert(2, "22.1");
//	insert(3, "25.0");
//	
	List<Long> results = getCounts(10);
//	System.out.println(results);
	//compare();
	runTransaction();
}


private void insert(Integer minuteOfDay, String measurement) {
	Jedis jedis = new Jedis();
    jedis.zadd("metrics", minuteOfDay, measurement);
    System.out.println(jedis.zcard("metrics"));
    jedis.close();
}


private List<Long> getCounts(Integer num) {
	Jedis je = new Jedis();
	
	je.set("0","0");
	je.set("1","1");
	je.set("2","2");
	je.set("3","3");
	je.set("4","4");
	je.set("5","5");
	je.set("6","6");
	je.set("7","7");
	je.set("8","8");
	je.set("9","9");
	je.set("10","10");
	//System.out.println(je.exists("1"));
	List<Long> results = new ArrayList<>(num);
    for (int i=0; i<num; i++) {
        String key = String.valueOf(i);
        System.out.println("key " +key);
        if (je.exists(key)) {
        	System.out.println("inside if");
        	
            Long c = je.zcount(key, "-inf", "+inf");
            System.out.println(c);
            results.add(c);
            je.expire(key, 1000000);
        }
    }
                        
    return results;
}
public void compare() {
	Jedis jedis = new Jedis();
    Pipeline p = jedis.pipelined();
    System.out.println("1");
    Response<Long> length = p.zcard("set");
    System.out.println("1");
    if (length.get() < 1000) {
        System.out.println("1");
        String element = "foo" + String.valueOf(Math.random());
        System.out.println("1");
        p.zadd("set", Math.random(), element);
        System.out.println("1");
    }
    System.out.println("1");
    p.sync();
}
public void runTransaction() {
	Jedis jedis = new Jedis();
    jedis.set("a", "foo");
    jedis.set("b", "bar");
    jedis.set("c", "baz");
    System.out.println(1);
    Transaction t = jedis.multi();
    System.out.println(1);
    Response<String> r1 = t.set("b", "1");
    System.out.println(1);
    Response<Long> r2 = t.incr("a");
    System.out.println(1);
    Response<String> r3 = t.set("c", "100");
    System.out.println(1);
    t.exec();
    System.out.println(1);
    r1.get();
    System.out.println(1);
    r2.get();
    System.out.println(1);
    r3.get();
}
}
