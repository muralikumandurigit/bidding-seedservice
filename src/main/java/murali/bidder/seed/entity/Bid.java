package murali.bidder.seed.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bid {

	private String bid;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
	private Date bid_time;
	
	private String email;
	
	private int old_price;
	
	private int new_price;
	
	private String sid;
}
