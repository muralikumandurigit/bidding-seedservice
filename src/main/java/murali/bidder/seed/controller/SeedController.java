package murali.bidder.seed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import murali.bidder.seed.entity.Seed;
import murali.bidder.seed.service.SeedService;

@RestController
@Slf4j
@RequestMapping("/seeds")
public class SeedController {

	@Autowired
	private SeedService seedService;
	
	/*
	 * {
  "cid" : "003795b6-f6fb-4616-91b1-ee64853fe071",
  "start_date" : "2017-04-12 17:04:42",
  "email" : "muralikumanduri@gmail.com",
  "end_date" : "2017-04-12 17:04:42",
  "increment" : true,
  "seed_price" : 500
}
	 */
	@PostMapping("/")
	public Seed saveSeed(@RequestBody Seed seed) {
		log.info("Saving seed " + seed.toString());
		return seedService.saveSeed(seed);
	}
	
	@GetMapping("/{sid}")
	public Seed getSeed(@PathVariable String sid) {
		log.info("Validating seed " + sid);
		return seedService.getSeed(sid);
	}
	
	@PostMapping("/updatewinningbid")
	public Seed updateWinningBid(@RequestBody Seed seed) {
		log.info("Updating the winning bid");
		return seedService.updateWinningBid(seed);
	}
}
