package murali.bidder.seed.service;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;
import murali.bidder.seed.entity.Bid;
import murali.bidder.seed.entity.Seed;
import murali.bidder.seed.repository.SeedRepository;

@Service
@Slf4j
public class SeedService {

	@Autowired
	private SeedRepository seedRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	ExecutorService executors;
	
	@Value("${user.service.endpoint}")
	private String userServiceEndpoint;
	
	@Value("${commodity.service.endpoint}")
	private String commodityServiceEndpoint;

	@Value("${bid.service.endpoint}")
	private String bidServiceEndpoint;
	
	private Boolean isValidUser(String email) {
		return restTemplate.getForObject(userServiceEndpoint + email, Boolean.class);
	}
	
	private Boolean isValidCommodity(String cid) {
		return restTemplate.getForObject(commodityServiceEndpoint + cid, Boolean.class);
	}
	
	private void checkForSeedValidity(Seed seed) {
		Future<Boolean> isValidUserFutureObject = executors.submit(new Callable<Boolean>() {

			@Override
			public Boolean call() throws Exception {
				return isValidUser(seed.getEmail());
			}
			
		});
		
		Future<Boolean> isValidCommodityFutureObject = executors.submit(new Callable<Boolean>() {

			@Override
			public Boolean call() throws Exception {
				return isValidCommodity(seed.getCid());
			}
			
		});

		try {
			if (!isValidUserFutureObject.get()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user exist with the email " + seed.getEmail());
			}
		} catch (InterruptedException | ExecutionException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to reach userService " + e.getLocalizedMessage());
		}
		
		try {
			if (!isValidCommodityFutureObject.get()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No commodity exist with the id " + seed.getCid());
			}
		} catch (InterruptedException | ExecutionException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to reach commodityService " + e.getLocalizedMessage());
		}
	}
	
	public Seed saveSeed(Seed seed) {
		checkForSeedValidity(seed);
		seed.setSid(UUID.randomUUID().toString());
		Bid bid = saveSeedBid(seed);
		seed.setSeed_bid(bid.getBid());
		try {
			return seedRepository.save(seed);
		}
		catch(Exception e) {
			// Delete the seed bid
			restTemplate.delete(bidServiceEndpoint + "deletebid/" + bid.getBid());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Exception occurred while saving seed " + seed.getCid() + " with error " + e.getLocalizedMessage());
		}
	}
	
	private Bid saveSeedBid(Seed seed) {
		Bid bid = new Bid();
		bid.setEmail(seed.getEmail());
		log.info("seed price = " + seed.getSeed_price());
		bid.setNew_price(seed.getSeed_price());
		bid.setSid(seed.getSid());
		log.info("seed info " + seed.toString());
		log.info("Saving seed bid " + bid.toString());
		return restTemplate.postForObject(bidServiceEndpoint + "seedbid", bid, Bid.class);
	}
	
	public Boolean isValidSeed(String sid) {
		return seedRepository.findBySid(sid) == null ? false : true;
	}
}
