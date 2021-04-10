package murali.bidder.seed.entity.datatype.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.cassandra.core.convert.CassandraCustomConversions;

@Configuration
public class CassandraConfig {

    public CassandraCustomConversions customConversions() {
        List<Converter> converters = new ArrayList<>();
        converters.add(new ZonedDateTimeToDateConverter());
        converters.add(new DateToZonedDateTimeConverter());
        return new CassandraCustomConversions(converters);
    }
}
