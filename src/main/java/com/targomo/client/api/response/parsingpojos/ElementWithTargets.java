package com.targomo.client.api.response.parsingpojos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
public class ElementWithTargets<A> extends ElementWithId {

    @Getter @Setter
    private List<A> targets;
}
