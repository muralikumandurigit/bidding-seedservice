package murali.bidder.seed.entity.datatype.converter;

import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class ZonedDateTimeToDateConverter implements Converter<ZonedDateTime, Date> {
	@Override
	public Date convert(ZonedDateTime source) {
		return Date.from(source.toInstant());
	}
}
