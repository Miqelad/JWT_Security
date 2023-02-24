/**
 * @author BomboRa
 */
package com.miq.security.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsInfoHeader {
    private String typ;
    private String alg;
}
