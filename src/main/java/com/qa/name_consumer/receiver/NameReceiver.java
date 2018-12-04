package com.qa.name_consumer.receiver;

import org.springframework.stereotype.Component;

import com.qa.name_consumer.persistence.repository.NameRepository;
import com.qa.name_crud_api.persistence.domain.BabyNamePOJO;

import org.springframework.jms.annotation.JmsListener;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@Component
public class NameReceiver {

	@Autowired
	private NameRepository repo;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@JmsListener(destination = "${queue.saveToDB}", containerFactory = "myFactory")
	public void receiveStoreMessage(BabyNamePOJO name) {
		repo.insert(name);
	}
	
	@JmsListener(destination = "${queue.updateDB}", containerFactory = "myFactory")
	public void receiveUpdateMessage(BabyNamePOJO name) {
		deleteEntry(name);
		repo.insert(name);
	}
	
	@JmsListener(destination = "${queue.deleteFromDB}", containerFactory = "myFactory")
	public void receiveDeleteMessage(BabyNamePOJO name) {
		deleteEntry(name);
	}
	
	private void deleteEntry(BabyNamePOJO name) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(name.getId()));
		List<BabyNamePOJO> names = mongoTemplate.find(query, BabyNamePOJO.class);
		
		for (BabyNamePOJO nameToDelete: names) {
			repo.delete(nameToDelete);
		}
	}
}
