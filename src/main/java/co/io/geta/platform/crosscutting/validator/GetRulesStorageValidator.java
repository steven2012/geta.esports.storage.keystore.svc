package co.io.geta.platform.crosscutting.validator;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.io.geta.platform.crosscutting.constants.GetaConstants;
import co.io.geta.platform.crosscutting.exception.EBusinessException;
import co.io.geta.platform.crosscutting.payload.GetRulesStoragePayload;
import co.io.geta.platform.crosscutting.util.MessageUtil;
import lombok.Getter;
import lombok.Setter;
import okhttp3.internal.http2.ErrorCode;

@Component
@Getter
@Setter
public class GetRulesStorageValidator {

	@Autowired
	private MessageUtil messagesUtil;

	public void payloadValidator(GetRulesStoragePayload payload) throws EBusinessException {

		if (payload.getKeyRule() == null) {
			throw new EBusinessException(
					MessageFormat.format(messagesUtil.getMessage(GetaConstants.PARAM_NOT_NULL), "KeyRule"), ErrorCode.CANCEL);
		}

		if (payload.getKeyRule().isEmpty()) {

			throw new EBusinessException(
					MessageFormat.format(messagesUtil.getMessage(GetaConstants.PARAM_NOT_EMPTY), "KeyRule"), ErrorCode.CANCEL);
		}
	}

}
