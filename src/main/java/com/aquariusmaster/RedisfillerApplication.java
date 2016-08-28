package com.aquariusmaster;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DockerClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class RedisfillerApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisfillerApplication.class);

	private static boolean isDockerStarted;
	private static int skip = 50;
	private static int counter;

	@Resource
	private Environment environment;

	@Autowired
	private BitcoinRecordRepository bitcoinRecordRepository;

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(RedisfillerApplication.class, args);

	}
	@Scheduled(fixedDelay = 2000)
	public void scheduler(){
		LOGGER.info("Scheduler started");

		if (!isDockerStarted){
			dockerStarter();
		}

		List<BitcoinRecord> records =
				XMLRecordReader.readRecordFromXML(environment.getProperty("redis.data"), skip, counter);
		counter+=skip;
		LOGGER.info("Retrived records: " + records);
		if (records != null && records.size() == 0){
			LOGGER.info("Job done. Exiting ...");
			System.exit(0);
		}
		LOGGER.info("Scheduler: trying save in redis");
		bitcoinRecordRepository.saveRecords(records);
		LOGGER.info("Data saved!");

	}

	private void dockerStarter(){

		DockerClient dockerClient = DockerClientBuilder.getInstance("unix:///var/run/docker.sock").build();
		Info info = dockerClient.infoCmd().exec();
		LOGGER.info("Docker info: " + info);
		LOGGER.info(info.getContainersRunning() + " is already containers run");

		ExposedPort tcp6379 = ExposedPort.tcp(6379);
		Ports portBindings = new Ports();
		portBindings.bind(tcp6379, new Ports.Binding("0.0.0.0", "6379"));

		CreateContainerResponse container = dockerClient.createContainerCmd("redis")
				.withCmd("redis-server")
				.withExposedPorts(tcp6379)
				.withPortBindings(portBindings)
				.exec();

		try {
			dockerClient.startContainerCmd(container.getId()).exec();
			isDockerStarted = true;
			LOGGER.info("Container run with ID = " + container.getId());
		} catch (DockerException e){
			LOGGER.error("DockerException", e);
			System.exit(1);
		}

	}
}
