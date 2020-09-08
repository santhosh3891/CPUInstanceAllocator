# CPUInstanceAllocator
Module to allocate CPU instances in an optimized way.

This allocator is responsible for allocating servers for users based on their needs. 

There are 6 server types and each server type has N number of CPUs. Each region might have all the server types or the combination of below server types,
  * large - 1 CPU
  * xlarge - 2 CPUs
  * 2xlarge - 4 CPUs
  * 4xlarge - 8 CPUs
  * 8xlarge - 16 CPUs
  * 10xlarge - 32 CPUs

The cost per hour for each server type will vary for each data centre region. 

User can request for CPUs in the following ways,
   * requires minimum N number of CPUs for H hours,
   * requires as many possible N number of CPUs for given P price for H hours,
   * or the combination of both (i.e.) requires minumum N number of CPUs for H hours and don't want to pay more than P price.

# Test Cases and Sample I/O:
Below is the cost per hour of each server type for regions "us-east" and "us-west"<br/>
{
	"us-east":{
		"large":0.12, 
		"xlarge":0.23,
		"2xlarge":0.45,
		"4xlarge":0.774,
		"8xlarge":1.4,
		"10xlarge":2.82
    },
	"us-west":{
		"large":0.14, 
		"2xlarge":0.413,
		"4xlarge":0.89,
		"8xlarge":1.3,
		"10xlarge":2.97
    }
}

NOTE: Output will be sorted in ascending order based on the total cost

# Test Case #1:
Input : User requests for 150 CPUs for 6 hours<br/>
Output : [
   {
      "region":"us-east",
      "totalCost":"$85.22",
      "servers":[
         {
            "serverType":"large",
            "serverCount":6
         },
         {
            "serverType":"xlarge",
            "serverCount":4
         },
         {
            "serverType":"2xlarge",
            "serverCount":4
         },
         {
            "serverType":"4xlarge",
            "serverCount":3
         },
         {
            "serverType":"8xlarge",
            "serverCount":2
         },
         {
            "serverType":"10xlarge",
            "serverCount":2
         }
      ]
   },
   {
      "region":"us-west",
      "totalCost":"$87.56",
      "servers":[
         {
            "serverType":"large",
            "serverCount":6
         },
         {
            "serverType":"2xlarge",
            "serverCount":4
         },
         {
            "serverType":"4xlarge",
            "serverCount":4
         },
         {
            "serverType":"8xlarge",
            "serverCount":2
         },
         {
            "serverType":"10xlarge",
            "serverCount":2
         }
      ]
   }
]

# Test Case #2:
Input : User requests for as many possible CPUs for $65 for 3 hours <br/>
Output : [
   {
      "region":"us-west",
      "totalCost":"$64.82",
      "servers":[
         {
            "serverType":"large",
            "serverCount":7
         },
         {
            "serverType":"2xlarge",
            "serverCount":5
         },
         {
            "serverType":"4xlarge",
            "serverCount":5
         },
         {
            "serverType":"8xlarge",
            "serverCount":4
         },
         {
            "serverType":"10xlarge",
            "serverCount":3
         }
      ]
   },
   {
      "region":"us-east",
      "totalCost":"$64.88",
      "servers":[
         {
            "serverType":"large",
            "serverCount":7
         },
         {
            "serverType":"xlarge",
            "serverCount":6
         },
         {
            "serverType":"2xlarge",
            "serverCount":5
         },
         {
            "serverType":"4xlarge",
            "serverCount":4
         },
         {
            "serverType":"8xlarge",
            "serverCount":4
         },
         {
            "serverType":"10xlarge",
            "serverCount":3
         }
      ]
   }
]

# Test Case #3: If CPUs cannot be allocated for given price and hours, then the following message will be printed
Input : User request for 150 CPUs for $50 for 5 hours <br/>
Output : CPUAllocationException occurred: Expected CPU count 150 cannot be allocated with the given price $50.0 in the region: us-east for given hours: 5<br/>
CPUAllocationException occurred: Expected CPU count 150 cannot be allocated with the given price $50.0 in the region: us-west for given hours: 5

# Test Case #4:
Input : User requests for 150 CPUs for $200 for 6 hours <br/>
Output : [
   {
      "region":"us-west",
      "totalCost":"$102.84",
      "servers":[
         {
            "serverType":"large",
            "serverCount":3
         },
         {
            "serverType":"2xlarge",
            "serverCount":3
         },
         {
            "serverType":"4xlarge",
            "serverCount":3
         },
         {
            "serverType":"8xlarge",
            "serverCount":3
         },
         {
            "serverType":"10xlarge",
            "serverCount":3
         }
      ]
   },
   {
      "region":"us-east",
      "totalCost":"$104.3",
      "servers":[
         {
            "serverType":"large",
            "serverCount":3
         },
         {
            "serverType":"xlarge",
            "serverCount":3
         },
         {
            "serverType":"2xlarge",
            "serverCount":3
         },
         {
            "serverType":"4xlarge",
            "serverCount":3
         },
         {
            "serverType":"8xlarge",
            "serverCount":3
         },
         {
            "serverType":"10xlarge",
            "serverCount":3
         }
      ]
   }
]
