package com.rawcod.jerminal.command.parameters;

import com.google.common.base.Optional;
import com.rawcod.jerminal.returnvalue.autocomplete.AutoCompleteErrors;
import com.rawcod.jerminal.returnvalue.autocomplete.AutoCompleteReturnValue;
import com.rawcod.jerminal.returnvalue.parse.ParseErrors;
import com.rawcod.jerminal.returnvalue.parse.param.ParseParamValueReturnValue;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * User: ykrasik
 * Date: 25/07/2014
 * Time: 16:19
 */
public abstract class AbstractMandatoryCommandParam implements CommandParam {
    private final String name;
    private final String description;

    protected AbstractMandatoryCommandParam(String name, String description) {
        this.name = checkNotNull(name, "name is null!");
        this.description = checkNotNull(description, "description is null!");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public ParamType getType() {
        return ParamType.MANDATORY;
    }

    @Override
    public ParseParamValueReturnValue parse(Optional<String> rawValue, ParamParseContext context) {
        if (!rawValue.isPresent()) {
            return ParseErrors.paramNotBound(name);
        }
        return parse(rawValue.get(), context);
    }

    @Override
    public AutoCompleteReturnValue autoComplete(Optional<String> prefix, ParamParseContext context) {
        if (!prefix.isPresent()) {
            return AutoCompleteErrors.paramNotBound(name);
        }
        return autoComplete(prefix.get(), context);
    }

    protected abstract ParseParamValueReturnValue parse(String rawValue, ParamParseContext context);
    protected abstract AutoCompleteReturnValue autoComplete(String prefix, ParamParseContext context);

    @Override
    public String toString() {
        return name + ": " + description;
    }
}