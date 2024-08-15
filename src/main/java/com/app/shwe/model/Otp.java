
package com.app.shwe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "otp")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipient")
    private String recipient;

    @Column(name = "otp_code")
    private String otpCode;

    @Column(name = "ref_code")
    private String refCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiry_time")
    private Date expiryTime;
}
