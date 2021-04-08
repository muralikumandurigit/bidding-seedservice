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
	
	@PostMapping("/")
	public Seed saveSeed(@RequestBody Seed seed) {
		log.info("Saving seed " + seed.toString());
		return seedService.saveSeed(seed);
	}
	
	@GetMapping("/{sid}")
	public Boolean isValidSeed(@PathVariable String sid) {
		log.info("Validating seed " + sid);
		return seedService.isValidSeed(sid);
	}
}
