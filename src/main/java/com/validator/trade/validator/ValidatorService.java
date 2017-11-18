package com.validator.trade.validator;

import java.util.Collection;

public interface ValidatorService<ENTITY, RETURN_TYPE> {
	
	RETURN_TYPE validate(ENTITY entity);
	Collection<RETURN_TYPE> validateMultiple(Collection<ENTITY> entities);
	
}
