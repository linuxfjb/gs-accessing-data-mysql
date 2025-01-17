node {
	def application = "gs-accessing-data-mysql"
	def dockerhubaccountid = "linuxfjb"

	stage('Clone repository') {
		checkout scm
	}

	stage ('Nice Request springboot to stop') {
		//stop any previously running containers in this instance so spring boot does not have a port conflict
		sh ("docker stop springboot || echo \"container springboot does not exist yet.\"")
	}

	stage('Build image') {
		dir("${env.WORKSPACE}/complete") {
			sh ("./mvnw -DskipTests=true clean package")
			app = docker.build("${dockerhubaccountid}/${application}:${BUILD_NUMBER}")
		}
	}

	stage('Push image') {
		withDockerRegistry([ credentialsId: "docker_login", url: "" ]) {
			app.push()
			app.push("latest")
		}
	}

	stage('Integration Test') {
		dir("${env.WORKSPACE}/complete") {
			sh ("docker-compose -f docker-compose.yml up -d")
			sh ("./mvnw test")
		}
	}

	stage('Deploy MySQL and Spring Boot services in Docker Container') {
		dir("${env.WORKSPACE}/complete") {
			//if docker hasnt shut down by now, force it to stop and remove so next container can run
			sh ("docker rm -f springboot || echo \"container springboot does not exist.\"")

			sh ("docker-compose -f docker-compose_container.yml up -d")
			sh ("docker run --name springboot --network complete_mynetwork -p 8081:8081 --expose 8081 -e spring.datasource.url=\"jdbc:mysql://db:3306/db_example\" -d ${dockerhubaccountid}/${application}:${BUILD_NUMBER}")
			sh ("echo \"You can connect http://34.150.254.230:8081/demo/all . This is a statically reserved IP in GCP.\"")
		}
	}

	stage('Remove old images') {
		// remove docker pld images
		// sh("docker rmi ${dockerhubaccountid}/${application}:latest -f")
		sh ("docker image prune -f")
		sh ("docker container prune -f")
	}
}
