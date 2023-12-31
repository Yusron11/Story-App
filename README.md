# Story App 

## Google Maps API Key Configuration

This application utilizes Google Maps services for location-related functionalities. To enable access to these services, you need to replace the API KEY in the `Manifest.xml` file with your own API KEY.

### How to Obtain a Google Maps API Key

1. Go to the [Google Maps Platform Credentials page](https://developers.google.com/maps/documentation/embed/get-api-key?hl=en#:~:text=Go%20to%20the%20Google%20Maps%20Platform%20%3E%20Credentials%20page).

2. On the Credentials page, click on the "Create Credentials" dropdown and select "API Key."

3. If prompted, choose the project you want to associate with your API Key or create a new project.

4. Copy the generated API Key.

5. Open the `Manifest.xml` file in your Story App project.

6. Locate the following section:

    ```xml
    <meta-data
        android:name="com.google.android.geo.API_KEY"
        android:value="YOUR_API_KEY_HERE" />
    ```

7. Replace `"YOUR_API_KEY_HERE"` with the API Key you obtained from the Google Cloud Console.

8. Save the changes to the `Manifest.xml` file.

9. You can now build and run your Story App with the updated API Key for Google Maps services.

### Additional Information

- **Note:** Keep your API Key secure and do not expose it publicly, as it can be misused if accessed by unauthorized users.

- For more detailed information on obtaining and managing Google Maps API Keys, refer to the [Google Maps Platform documentation](https://developers.google.com/maps/documentation/embed/get-api-key?hl=en#:~:text=Go%20to%20the%20Google%20Maps%20Platform%20%3E%20Credentials%20page). 

- If you encounter any issues or have questions related to the Google Maps API, consult the [Google Cloud Support](https://cloud.google.com/support) for assistance.

Thank you for using Story App! If you have any further questions or concerns, feel free to reach out to our support team.
