package murali.bidder.seed.entity.datatype.converter;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class DateToZonedDateTimeConverter implements Converter<Date, ZonedDateTime> {
	@Override
	public ZonedDateTime convert(Date source) {
		return ZonedDateTime.ofInstant(source.toInstant(),
                ZoneId.systemDefault());
	}
}
