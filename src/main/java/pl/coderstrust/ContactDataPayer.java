package pl.coderstrust;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ContactDataPayer {
    private @NonNull String email;
    private @NonNull String phoneNumber;
}
