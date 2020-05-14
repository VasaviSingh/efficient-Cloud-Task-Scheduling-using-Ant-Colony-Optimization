package org.cloudbus.cloudsim.examples;

import java.util.ArrayList; //The ArrayList class is a resizable array, which can be found in the java.util package.

import java.util.Calendar;   //Calendar class in Java is an abstract class that provides methods for converting date between a specific instant in time and 
a set of calendar fields such as MONTH, YEAR, HOUR, 

import java.util.LinkedList;  //Linked List are linear data structures where the elements are not stored in contiguous locations and every element 
is a separate object with a data part and address part. The elements are linked using pointers and addresses.

import java.util.List;   //The Java.util.List is a child interface of Collection. It is an ordered collection of objects in which duplicate values can be stored.
 Since List preserves the insertion order, it allows positional access and insertion of elements. List Interface is implemented by the classes of 
ArrayList, LinkedList, Vector and Stack.

import java.util.Random;  //Random class is used to generate pseudo-random numbers in java. 

import java.io.*;  //Import all the libraries in java

Import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.Cloudlet;

import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;    //The “CloudletScheduler” is an abstract  class that defines the basic skeleton to implement the policy 
to be used for cloudlet scheduling to be performed by a virtual machine. 

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;

import org.cloudbus.cloudsim.core.CloudSim;  //This package contains the main classes of this project and are directly responsible for initiating(CloudInformationService.java, Cloudsim.java), starting(Cloudsim.java), maintaining(
Cloudsim.java, SimEntity.java, SimEvent.java, FutureQueue.java and DeferedQueue.java) and end the simulation process(Cloudsim.java). 

import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.examples.ChartGraph;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;

public class GraphicalRepresentation {

	/** The cloudlet list. */
	private static List<Cloudlet> cloudletList;
	private static List<Cloudlet> cloudletList1;

	/** The vmlist. */
	private static List<Vm> vmlist;
	private static List<Vm> vmlist1;

	private static List<Vm> createVM(int userId, int vms) {

		//Creates a container to store VMs. This list is passed to the broker later
		LinkedList<Vm> list = new LinkedList<Vm>();

		//VM Parameters
		long size = 10000; //storage  size (MB)
		int ram = 512; //vm memory (MB)
		int mips = 1000;
		long bw = 1000; //bandwidth is the maximum rate of data transfer across a given path.
		int pesNumber = 1; //number of cpus
		String vmm = "Xen"; //VM Monitor name
		Random r = new Random(1);

		//create VMs
		Vm[] vm = new Vm[vms];

		for(int i=0;i<vms;i++)
		{
			mips = 500 + r.nextInt(500);
			vm[i] = new Vm(i, userId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());

			//for creating a VM with a space shared scheduling policy for cloudlets:
			//vm[i] = Vm(i, userId, mips, pesNumber, ram, bw, size, priority, vmm, new CloudletSchedulerSpaceShared());

			list.add(vm[i]);
		}

		return list;
	}

// userId: defines the broker id to through which this cloudlet will be executed and the result of execution will be returned & displayed on the console through the same broker.

cloudletLength: defines the length of the cloudlet in terms of the expected number of instructions required to be executed in the lifetime of the workload under execution.

cloudletFileSize: As the cloudlet is supposed to be executed over the cloud that means the input data transfer over the network should be happening during the execution.
This attribute defines the total size of the input data in bytes.

cloudletOutputSize: Similar to cloudletFileSize the output data size is also defined in bytes.

numberOfPes: Defines the number of processors required to execute this cloudlet, if parallel processing is required to be performed.
cloudletId: used as a unique identification number for cloudlets, required for referencing throughout the simulation flow.

status: defines the current state of the cloudlet. A cloudlet could be in any one of the state as follows: CREATED, READY, QUEUED, INEXEC, SUCCESS, FAILED, CANCELED, PAUSED, RESUMED, FAILED_RESOURCE_UNAVAILABLE. These states are defined explicitly in cloudlet model class and are assigned a numerical value.
num: defined the time at various stages of cloudlet execution

execStartTime: Defines the latest execution time and is updated every time the status of cloudlet shuffle among CANCEL, PAUSED and RESUMED
finishTime: Stores the time, when the execution of the cloudlet is finished and status is not a success.

resList: Maintains the log of the resources where the cloudlet is being executed.
index: defines the ID of the resource on which the cloudlet is being currently assigned and helps in getting the status
classType: Defines the resource scheduling information(never used in cloudsim 3.0.3)
netToS: Defines the type of service for the network transactions. This is a field for IPv6 for ensuring the quality of service.
vmId: stores the Id of the virtual machine to which this cloudlet is to be allocated for allocation. This is done in the case where you are planning to move a real-life application to a cloud-based system with a specific Virtual machine configuration. Such mapping of cloudlet helps in determining the behavior of the application in various scenarios as simulated over the cloudsim simulation engine.

utilizationModelCpu/utilizationModelRam/utilizationModelBw: these attributes defines the type of model to be used for executing the defined cloudlet.

 It may individually defined for each attribute from following: UtilizationModelFull, UtilizationModelNull, UtilizationModelStochastic and UtilizationModelPlanetLabInMemory
(used in power-aware workloads only).
requiredFiles: defines the path for the list of files to be required by the cloudlet during execution. These are the input files and are directly associated with the network bandwidth cost.


	private static List<Cloudlet> createCloudlet(int userId, int cloudlets){
		// Creates a container to store Cloudlets
		LinkedList<Cloudlet> list = new LinkedList<Cloudlet>();

		//cloudlet parameters
		long length ;
		long fileSize = 300;
		long outputSize = 300;
		int pesNumber = 1;

		UtilizationModel utilizationModel = new UtilizationModelFull();//Provides classes that model utilization of resources such as CPU, Ram and Bandwidth, defining how a given resource is used by a Cloudlet along the simulation time.
		
		//The UtilizationModelFull class is a simple model, according to which a Cloudlet always utilize all the available CPU capacity.

		Random r1 = new Random(1);
		// Random r2 = new Random(2);

		Cloudlet[] cloudlet = new Cloudlet[cloudlets];

		for(int i=0;i<cloudlets;i++){
			length = 100 + r1.nextInt(900);
			// fileSize = 100 + r2.nextInt(200);

			cloudlet[i] = new Cloudlet(i, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			// setting the owner of these Cloudlets

			cloudlet[i].setUserId(userId);
			list.add(cloudlet[i]);
		}

		return list;
	}


	public static void main(String[] args) {
		
		try {
			// First step: Initialize the CloudSim package. It should be called
			// before creating any entities.

			int num_user = 1;   // number of grid users(cluster grid, A cluster grid provides a single point of access to users in a single project or a single department.). 
				                 This user count is directly proportional to a number of brokers in the current simulation.

			Calendar calendar = Calendar.getInstance(); //return a Calendar instance based on the current time in the default time zone with the default locale.

			boolean trace_flag = false;  // mean trace events
			
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();  //It creates new empty dataset.
			
			int i=20;	

			for (int k=1;k<=10;k++){
				CloudSim.init(num_user, calendar, trace_flag);

				// Second step: Create Datacenters
				//Datacenters are the resource providers in CloudSim. We need at list one of them to run a CloudSim simulation
				
				Datacenter datacenter0 = createDatacenter("Datacenter_0",i);
				Datacenter datacenter1 = createDatacenter("Datacenter_1",i);
				
				//Third step: Create Broker

				SJFBroker broker = createBroker();
				DatacenterBroker broker1 = createBroker1();
				
				int brokerId = broker.getId();
				int brokerId1 = broker1.getId();
				
				vmlist = createVM(brokerId,i);
				vmlist1 = createVM(brokerId1,i);
				
				
				cloudletList = createCloudlet(brokerId,100*k);
				cloudletList1 = createCloudlet(brokerId1,100*k);
				Log.printLine(cloudletList.size());
				
				broker.submitVmList(vmlist);
				broker.submitCloudletList(cloudletList);
				
				broker1.submitVmList(vmlist1);
				broker1.submitCloudletList(cloudletList1);
				
				// Fifth step: Starts the simulation
				CloudSim.startSimulation();
				
				// Final step: Print results when simulation is over
				List<Cloudlet> newList = broker.getCloudletReceivedList();
				List<Cloudlet> newList1 = broker1.getCloudletReceivedList();
				
				CloudSim.stopSimulation();
				
				
				double max = 0;
				for (int j=0;j<100*k;j++){
					if(max < newList.get(j).getFinishTime())
						max = newList.get(j).getFinishTime();
				}
				double min = 10000000;
				for (int j=0;j<100*k;j++){
					if(min > newList.get(j).getExecStartTime())
						min = newList.get(j).getExecStartTime();
				}
				
				double max1 = 0;
				for (int j=0;j<100*k;j++){
					if(max1 < newList1.get(j).getFinishTime())
						max1 = newList1.get(j).getFinishTime();
				}
				double min1 = 10000000;
				for (int j=0;j<100*k;j++){
					if(min1 > newList1.get(j).getExecStartTime())
						min1 = newList1.get(j).getExecStartTime();
				}
				
				dataset.addValue((Number)(max-min),"LBACO Algo",String.valueOf(100*k));
				dataset.addValue((Number)(max1-min1),"SJF Algo",String.valueOf(100*k));
			}
	
			ChartGraph chart=new ChartGraph("Graphical Representation(TITLE)","Response Time Vs. Cloudlet(CategoryAxisLabel)","No. of cloudlets(VAlue Axis LAbel)",
                                                                                             "Total response time(Axis label)",dataset);

			chart.pack( );//fit accor to the screen size

		      RefineryUtilities.centerFrameOnScreen( chart );//Positions the specified frame in the middle of the screen.

		      chart.setVisible( true );
			//obj.implement("Response Time Vs. Cloudlet","No. of cloudlets","Total response time",dataset);

				}
		catch (Exception e)
		{
			e.printStackTrace();
			Log.printLine("The simulation has been terminated due to an unexpected error");
		}
	}

	private static Datacenter createDatacenter(String name, int elements){

		// Here are the steps needed to create a PowerDatacenter:
		// 1. We need to create a list to store one or more
		//    Machines
		List<Host> hostList = new ArrayList<Host>();

		// 2. A Machine contains one or more PEs or CPUs/Cores. Therefore, should
		//    create a list to store these PEs before creating
		//    a Machine.
		List<Pe> peList1 = new ArrayList<Pe>();

		int mips = 1000;

		// 3. Create PEs and add these into the list.
		//for a quad-core machine, a list of 4 PEs is required:
		for (int i=0;i<elements;i++){
			peList1.add(new Pe(i, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
		}
		
		//4. Create Hosts with its id and list of PEs and add them to the list of machines
		int hostId=0;
		int ram = 25600; //host memory (MB)
		long storage = 1000000; //host storage
		int bw = 50000;

		hostList.add(
    			new Host(
    				hostId,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList1,
    				new VmSchedulerSpaceShared(peList1)
    			)
    		); 



		// 5. Create a DatacenterCharacteristics object that stores the
		//    properties of a data center: architecture, OS, list of
		//    Machines, allocation policy: time- or space-shared, time zone
		//    and its price (G$/Pe time unit).
		String arch = "x86";      // system architecture
		String os = "Linux";          // operating system
		String vmm = "Xen";
		double time_zone = 10.0;         // time zone this resource located
		double cost = 3.0;              // the cost of using processing in this resource
		double costPerMem = 0.05;		// the cost of using memory in this resource
		double costPerStorage = 0.1;	// the cost of using storage in this resource
		double costPerBw = 0.1;			// the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<Storage>();	//we are not adding SAN devices by now

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);


		// 6. Finally, we need to create a PowerDatacenter object.
		Datacenter datacenter = null;
		try {
			datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datacenter;
	}

	//We strongly encourage users to develop their own broker policies, to submit vms and cloudlets according
	//to the specific rules of the simulated scenario
	public static SJFBroker createBroker(){

		SJFBroker broker = null;
		try {
			//int m, double Q, double alpha, double beta, double gamma, double rho
			broker = new SJFBroker("Broker",37,1.0,2.0,1.0,4.0,0.05);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}

	public static DatacenterBroker createBroker1(){

		DatacenterBroker broker = null;
		try {
			broker = new DatacenterBroker("Broker",37,1.0,2.0,1.0,4.0,0.05);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}

}