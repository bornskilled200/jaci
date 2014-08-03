package com.rawcod.jerminal.command.parameters.optional;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.rawcod.jerminal.command.parameters.CommandParam;
import com.rawcod.jerminal.command.parameters.ParamParseContext;
import com.rawcod.jerminal.command.parameters.ParamType;
import com.rawcod.jerminal.returnvalue.autocomplete.AutoCompleteReturnValue;
import com.rawcod.jerminal.returnvalue.parse.param.ParseParamValueReturnValue;

/**
 * User: ykrasik
 * Date: 25/07/2014
 * Time: 21:02
 */
public class OptionalParam<T> implements CommandParam {
    private final CommandParam delegate;
    private final Supplier<T> defaultValueSupplier;

    public OptionalParam(CommandParam delegate, Supplier<T> defaultValueSupplier) {
        this.delegate = delegate;
        this.defaultValueSupplier = defaultValueSupplier;
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public String getDescription() {
        return delegate.getDescription();
    }

    @Override
    public ParamType getType() {
        return ParamType.OPTIONAL;
    }

    @Override
    public ParseParamValueReturnValue parse(Optional<String> rawValue, ParamParseContext context) {
        if (rawValue.isPresent()) {
            return delegate.parse(rawValue, context);
        }
        return ParseParamValueReturnValue.success(defaultValueSupplier.get());
    }

    @Override
    public AutoCompleteReturnValue autoComplete(Optional<String> prefix, ParamParseContext context) {
        return delegate.autoComplete(prefix, context);
    }

    @Override
    public String toString() {
        return '[' + delegate.toString() + ']';
    }
}