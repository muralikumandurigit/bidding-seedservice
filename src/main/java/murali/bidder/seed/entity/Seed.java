package murali.bidder.seed.entity;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("seeds")
public class Seed {

	@PrimaryKey
	@Column
	private String sid;
	
	@Column
	private String cid;
	
	@Column
	private String email;
	
	@Column
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime start_date;
	
	@Column
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime end_date;
	
	@Column
	private Boolean increment;
	
	@Column
	private String seed_bid;
	
	@Column
	private String winner_email;
	
	@Column
	private String status;
	
	@Column
	private String winning_bid;
	
	@Transient
	private int seed_price;
}
