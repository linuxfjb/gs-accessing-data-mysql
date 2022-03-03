node {
	def application = "gs-accessing-data-mysql"
	def dockerhubaccountid = "linuxfjb"

	stage('Clone repository') {
		checkout scm
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
			//stop any previously running containers in this instance so spring boot does not have a port conflict
			sh ("sudo docker stop \$(sudo docker ps -q --filter status=running)")
			sh ("docker-compose -f docker-compose_container.yml up -d")
			sh ("docker run --network complete_mynetwork -p 8081:8081 --expose 8081 -e spring.datasource.url=\"jdbc:mysql://db:3306/db_example\" -d ${dockerhubaccountid}/${application}:${BUILD_NUMBER}")
		}
	}

	stage('Remove old images') {
		// remove docker pld images
		// sh("docker rmi ${dockerhubaccountid}/${application}:latest -f")
		sh ("docker image prune -f")
		sh ("docker container prune -f")
	}
}
