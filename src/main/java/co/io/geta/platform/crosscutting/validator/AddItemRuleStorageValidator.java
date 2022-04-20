package co.io.geta.platform.crosscutting.validator;

import java.text.MessageFormat;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.io.geta.platform.crosscutting.constants.GetaConstants;
import co.io.geta.platform.crosscutting.exception.EBusinessException;
import co.io.geta.platform.crosscutting.payload.AddItemRuleStoragePayload;
import co.io.geta.platform.crosscutting.util.MessageUtil;
import lombok.Getter;
import lombok.Setter;
import okhttp3.internal.http2.ErrorCode;

@Component
@Getter
@Setter
public class AddItemRuleStorageValidator {

	@Autowired
	private MessageUtil messagesUtil;

	public void payloadValidator(AddItemRuleStoragePayload payload) throws EBusinessException {

		if (payload.getRule() == null || payload.getRule().isEmpty()) {
			throw new EBusinessException(MessageFormat.format(messagesUtil.getMessage(GetaConstants.PARAM_NOT_NULL), "Rule"),
					ErrorCode.CANCEL);
		}

		if (!(payload.getRule() instanceof LinkedHashMap)) {

			throw new EBusinessException(MessageFormat.format(messagesUtil.getMessage(GetaConstants.PARAM_NOT_NULL), "Rule"),
					ErrorCode.CANCEL);

		}
	}
}
