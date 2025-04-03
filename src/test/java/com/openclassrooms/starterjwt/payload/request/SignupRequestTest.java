/*package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SignupRequestTest {

    @Test
    public void testEqualsWithSameObject() {
        // Test branche equals avec le même objet
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");

        // Test de la branche this == obj
        assertTrue(request.equals(request));
    }

    @Test
    public void testEqualsWithNull() {
        // Test branche equals avec null
        SignupRequest request = new SignupRequest();

        // Test de la branche obj == null
        assertFalse(request.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        // Test branche equals avec une classe différente
        SignupRequest request = new SignupRequest();
        Object obj = new Object();

        // Test de la branche getClass() != obj.getClass()
        assertFalse(request.equals(obj));
    }

    @Test
    public void testEqualsWithDifferentValues() {
        // Test branche equals avec des valeurs différentes
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test1@example.com");

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("test2@example.com");

        // Test des branches dans l'égalité des propriétés
        assertNotEquals(request1, request2);
    }

    @Test
    public void testEqualsWithSameValues() {
        // Test branche equals avec les mêmes valeurs
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test@example.com");
        request1.setFirstName("John");
        request1.setLastName("Doe");
        request1.setPassword("password");

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("test@example.com");
        request2.setFirstName("John");
        request2.setLastName("Doe");
        request2.setPassword("password");

        // Test des branches dans l'égalité des propriétés
        assertEquals(request1, request2);
    }

    @Test
    public void testEqualsWithNullProperties() {
        // Test branche equals avec des propriétés null
        SignupRequest request1 = new SignupRequest();
        // Tous les champs sont null

        SignupRequest request2 = new SignupRequest();
        // Tous les champs sont null

        // Test des branches pour les vérifications de null
        assertEquals(request1, request2);
    }

    @Test
    public void testEqualsWithMixedNullProperties() {
        // Test branche equals avec mélange de propriétés null et non-null
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test@example.com");
        // autres champs null

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("test@example.com");
        // autres champs null

        // Test des branches pour les vérifications de null
        assertEquals(request1, request2);
    }

    @Test
    public void testHashCodeWithAllFields() {
        // Test des branches dans hashCode
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("password");

        // Vérifier que hashCode ne lance pas d'exception
        int hashCode = request.hashCode();
        assertTrue(hashCode != 0);
    }

    @Test
    public void testHashCodeWithNullFields() {
        // Test des branches dans hashCode pour les champs null
        SignupRequest request = new SignupRequest();
        // Tous les champs sont null

        // Vérifier que hashCode avec null ne lance pas d'exception
        int hashCode = request.hashCode();
        assertNotNull(hashCode);
    }

    @Test
    public void testEqualsWithNullEmail() {
        // Premier objet avec email null
        SignupRequest request1 = new SignupRequest();
        request1.setFirstName("John");
        request1.setLastName("Doe");
        request1.setPassword("password");
        // email est null

        // Deuxième objet avec email non null
        SignupRequest request2 = new SignupRequest();
        request2.setEmail("test@example.com");
        request2.setFirstName("John");
        request2.setLastName("Doe");
        request2.setPassword("password");

        // Test des branches pour la vérification du champ email
        assertNotEquals(request1, request2);
        assertNotEquals(request2, request1);
    }

    @Test
    public void testEqualsWithOneNullEmail() {
        // Premier objet avec email null
        SignupRequest request1 = new SignupRequest();
        request1.setFirstName("John");
        request1.setLastName("Doe");
        request1.setPassword("password");
        // email est null

        // Deuxième objet avec email null aussi
        SignupRequest request2 = new SignupRequest();
        request2.setFirstName("John");
        request2.setLastName("Doe");
        request2.setPassword("password");
        // email est null

        // Test des branches pour la comparaison de deux emails null
        assertEquals(request1, request2);
    }

    @Test
    public void testEqualsWithNullFirstName() {
        // Premier objet avec firstName null
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test@example.com");
        // firstName est null
        request1.setLastName("Doe");
        request1.setPassword("password");

        // Deuxième objet avec firstName non null
        SignupRequest request2 = new SignupRequest();
        request2.setEmail("test@example.com");
        request2.setFirstName("John");
        request2.setLastName("Doe");
        request2.setPassword("password");

        // Test des branches pour la vérification du champ firstName
        assertNotEquals(request1, request2);
        assertNotEquals(request2, request1);
    }

    @Test
    public void testEqualsWithBothNullFirstName() {
        // Premier objet avec firstName null
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test@example.com");
        // firstName est null
        request1.setLastName("Doe");
        request1.setPassword("password");

        // Deuxième objet avec firstName null aussi
        SignupRequest request2 = new SignupRequest();
        request2.setEmail("test@example.com");
        // firstName est null
        request2.setLastName("Doe");
        request2.setPassword("password");

        // Test des branches pour la comparaison de deux firstName null
        assertEquals(request1, request2);
    }

    @Test
    public void testEqualsWithNullLastName() {
        // Premier objet avec lastName null
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test@example.com");
        request1.setFirstName("John");
        // lastName est null
        request1.setPassword("password");

        // Deuxième objet avec lastName non null
        SignupRequest request2 = new SignupRequest();
        request2.setEmail("test@example.com");
        request2.setFirstName("John");
        request2.setLastName("Doe");
        request2.setPassword("password");

        // Test des branches pour la vérification du champ lastName
        assertNotEquals(request1, request2);
        assertNotEquals(request2, request1);
    }

    @Test
    public void testEqualsWithBothNullLastName() {
        // Premier objet avec lastName null
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test@example.com");
        request1.setFirstName("John");
        // lastName est null
        request1.setPassword("password");

        // Deuxième objet avec lastName null aussi
        SignupRequest request2 = new SignupRequest();
        request2.setEmail("test@example.com");
        request2.setFirstName("John");
        // lastName est null
        request2.setPassword("password");

        // Test des branches pour la comparaison de deux lastName null
        assertEquals(request1, request2);
    }

    @Test
    public void testEqualsWithNullPassword() {
        // Premier objet avec password null
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test@example.com");
        request1.setFirstName("John");
        request1.setLastName("Doe");
        // password est null

        // Deuxième objet avec password non null
        SignupRequest request2 = new SignupRequest();
        request2.setEmail("test@example.com");
        request2.setFirstName("John");
        request2.setLastName("Doe");
        request2.setPassword("password");

        // Test des branches pour la vérification du champ password
        assertNotEquals(request1, request2);
        assertNotEquals(request2, request1);
    }

    @Test
    public void testEqualsWithBothNullPassword() {
        // Premier objet avec password null
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test@example.com");
        request1.setFirstName("John");
        request1.setLastName("Doe");
        // password est null

        // Deuxième objet avec password null aussi
        SignupRequest request2 = new SignupRequest();
        request2.setEmail("test@example.com");
        request2.setFirstName("John");
        request2.setLastName("Doe");
        // password est null

        // Test des branches pour la comparaison de deux passwords null
        assertEquals(request1, request2);
    }

    @Test
    public void testEqualsWithPartialMatch() {
        // Plusieurs objets avec différentes combinaisons de champs

        // Objet de référence
        SignupRequest reference = new SignupRequest();
        reference.setEmail("test@example.com");
        reference.setFirstName("John");
        reference.setLastName("Doe");
        reference.setPassword("password");

        // Même email, mais autres champs différents
        SignupRequest sameEmail = new SignupRequest();
        sameEmail.setEmail("test@example.com");
        sameEmail.setFirstName("Jane");
        sameEmail.setLastName("Smith");
        sameEmail.setPassword("different");

        // Même firstName, mais autres champs différents
        SignupRequest sameFirstName = new SignupRequest();
        sameFirstName.setEmail("other@example.com");
        sameFirstName.setFirstName("John");
        sameFirstName.setLastName("Smith");
        sameFirstName.setPassword("different");

        // Test des différentes branches de comparaison partielle
        assertNotEquals(reference, sameEmail);
        assertNotEquals(reference, sameFirstName);
    }
}*/