package com.targomo.client.api.response.parsingpojos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
public class TargetsObject<A> extends IdObject {

    @Getter @Setter
    private List<A> targets;
}
