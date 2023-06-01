import http from "k6/http";
import { sleep } from "k6";
import { SharedArray } from "k6/data";
import { Trend } from 'k6/metrics';

export const options = {
  ext: {
    loadimpact: {
      projectID: 3633668,
      // Test runs with the same name groups test runs together
      name: "SSM-LOAD-TEST-1"
    }
  },
  stages: [
    { duration: '20m', target: 50 }, // simulate ramp-up of traffic from 1 to 60 users over 5 minutes.
    { duration: '10m', target: 60 }, // stay at 60 users for 10 minutes
    { duration: '3m', target: 100 }, // ramp-up to 100 users over 3 minutes (peak hour starts)
    { duration: '10m', target: 100 }, // stay at 100 users for a short amount of time (peak hour)
    { duration: '3m', target: 60 }, // ramp-down to 60 users over 3 minutes (peak hour ends)
    { duration: '10m', target: 60 }, // continue at 60 for an additional 10 minutes
    { duration: '5m', target: 0 }, // ramp-down to 0 users
  ],
  thresholds: {
    http_req_duration: ['p(99.99)<1500'], // 99% of requests must complete below 1.5s
  },
};

const data = new SharedArray("ssm", function() {
  return JSON.parse(open('ssm-data.json')); // return array of tags
});

// Reading random customerData from the JSON file
const customerData = data[Math.floor(Math.random() * data.length)];

const rootUrl = "https://conductor-server:8085/customer";

const httpResTime = new Trend('http_res_time');

export default function () {
  const headers = { "Content-Type": "application/json" };

  // Send POST request and get the response
  const customerResponse = http.post(rootUrl +'/save/DBS_30_05_2023_14_25_06.24769305', JSON.stringify(customerData), { headers: headers });

  // Extract the customerId from the response
  const customerId = JSON.parse(customerResponse.body).customerId;

  // Use the customerId in the PUT methods
 const updateSalesStatusUrl =rootUrl+ '/salesReview/'+customerId ;
 const updateRmStatusUrl = rootUrl+'/rmReview/'+customerId ;
  const updateDocStatusUrl = rootUrl+'/docReview/'+customerId ;
  const sdcDocStatusUrl = rootUrl+'/sdc/'+customerId ;
  const welcomeUrl = rootUrl+'/welcome/'+customerId ;

  // PUT method 1: Update sales status
  http.put(updateSalesStatusUrl, JSON.stringify(customerResponse), { headers: headers });

  // PUT method 2: Update RM status
  http.put(updateRmStatusUrl, JSON.stringify(customerResponse), { headers: headers });

  // PUT method 3: Update document status
  http.put(sdcDocStatusUrl, JSON.stringify(customerResponse), { headers: headers });
  // PUT method 4: Update document status
  http.put(updateDocStatusUrl, JSON.stringify(customerResponse), { headers: headers });
  // PUT method 5: Welcome the customer
  http.put(welcomeUrl, JSON.stringify(customerResponse), { headers: headers });
sleep(1)

}
