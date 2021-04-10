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

import murali.bidder.seed.entity.Seed;
import murali.bidder.seed.repository.SeedRepository;

@Service
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
		// Not yet started
		seed.setStatus("N");
		return seedRepository.save(seed);
	}

	public Seed getSeed(String sid) {
		return seedRepository.findBySid(sid);
	}

	public Seed updateWinningBid(Seed seed) {
		return seedRepository.save(seed);
	}
}
