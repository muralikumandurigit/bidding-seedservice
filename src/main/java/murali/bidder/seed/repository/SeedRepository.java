package murali.bidder.seed.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import murali.bidder.seed.entity.Seed;

@Repository
public interface SeedRepository extends CassandraRepository<Seed, String> {

	public Seed findBySid(String sid);

}
