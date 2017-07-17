/*
  Copyright 2017, Google, Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package com.example.bigquery;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.Dataset;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Examples for authenticating to Google BigQuery.
 *
 * <p>See: https://cloud.google.com/bigquery/authentication
 */
public class AuthSnippets {

  // [START explicit_service_account]
  public static void explicit() throws IOException {
    // Load credentials from JSON key file.
    GoogleCredentials credentials;
    File credentialsPath = new File("service_account.json");  // TODO: update to your key path.
    try (FileInputStream serviceAccountStream = new FileInputStream(credentialsPath)) {
      credentials = ServiceAccountCredentials.fromStream(serviceAccountStream);
    }

    // Instantiate a client.
    BigQuery bigquery =
        BigQueryOptions.newBuilder().setCredentials(credentials).build().getService();

    // Use the client.
    for (Dataset dataset : bigquery.listDatasets().iterateAll()) {
      System.out.printf("%s%n", dataset.getDatasetId().getDataset());
    }
  }
  // [END explicit_service_account]

  // [START default_credentials]
  public static void implicit() {
    // Instantiates a client
    BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();

    // Use the client.
    System.out.println("Datasets:");
    for (Dataset dataset : bigquery.listDatasets().iterateAll()) {
      System.out.printf("%s%n", dataset.getDatasetId().getDataset());
    }
  }
  // [END default_credentials]

  public static void main(String... args) throws IOException {
    boolean validArgs = args.length == 1;
    String sample = "explicit";
    if (validArgs) {
      sample = args[0];
      if (!sample.equals("explicit") && !sample.equals("implicit")) {
        validArgs = false;
      }
    }

    if (!validArgs) {
      System.err.println("Expected auth type argument: implict|explict");
      System.exit(1);
    }

    if (sample.equals("implicit")) {
      implicit();
    } else {
      explicit();
    }
  }
}
