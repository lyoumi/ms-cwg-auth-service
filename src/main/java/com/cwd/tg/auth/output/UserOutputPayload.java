package com.cwd.tg.auth.output;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@AllArgsConstructor
@Data
public class UserOutputPayload {
    private String username;
    private Set<String> permissions;
}
