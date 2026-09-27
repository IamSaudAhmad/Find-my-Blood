package com.example.blooddonorfinder.util;

import java.util.List;
import java.util.Map;

/**
 * Static reference data for donor -> recipient blood group compatibility.
 * Standard whole-blood donation rules (not accounting for plasma/platelet-only donations).
 */
public class BloodCompatibility {

    // donor blood group -> list of recipient blood groups that donor can give to
    private static final Map<String, List<String>> DONOR_TO_RECIPIENTS = Map.of(
            "O-",  List.of("O-", "O+", "A-", "A+", "B-", "B+", "AB-", "AB+"),
            "O+",  List.of("O+", "A+", "B+", "AB+"),
            "A-",  List.of("A-", "A+", "AB-", "AB+"),
            "A+",  List.of("A+", "AB+"),
            "B-",  List.of("B-", "B+", "AB-", "AB+"),
            "B+",  List.of("B+", "AB+"),
            "AB-", List.of("AB-", "AB+"),
            "AB+", List.of("AB+")
    );

    private BloodCompatibility() {
        // static utility class — no instances
    }

    public static boolean isCompatible(String donorBloodGroup, String recipientBloodGroup) {
        List<String> canDonateTo = DONOR_TO_RECIPIENTS.get(donorBloodGroup);
        return canDonateTo != null && canDonateTo.contains(recipientBloodGroup);
    }

    public static List<String> compatibleRecipientsFor(String donorBloodGroup) {
        return DONOR_TO_RECIPIENTS.getOrDefault(donorBloodGroup, List.of());
    }
}
