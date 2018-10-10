package pl.coderstrust;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class ContactDataPayer {
    private @NonNull String email;
    private @NonNull String phoneNumber;
}
