node {
	def application = "gs-accessing-data-mysql"
	def dockerhubaccountid = "linuxfjb"

	stage('Clone repository') {
		checkout scm
	}

	stage('Build image') {
		app = docker.build("${dockerhubaccountid}/${application}:${BUILD_NUMBER}")
	}

	stage('Push image') {
		withDockerRegistry([ credentialsId: "docker_login", url: "" ]) {
			app.push()
			app.push("latest")
		}
	}

	stage('Test') {
		sh ("cd complete")
		sh ("docker-compose -f docker-compose.yml up -d")
		sh ("./mvnw test")
	}

	stage('Deploy') {
		sh ("docker-compose -f docker-compose_container.yml up -d")
		sh ("docker run --network complete_mynetwork -p 8080:8080 -e spring.datasource.url="jdbc:mysql://db:3306/db_example" -d ${dockerhubaccountid}/${application}:${BUILD_NUMBER}")

	}

	stage('Remove old images') {
		// remove docker pld images
		// sh("docker rmi ${dockerhubaccountid}/${application}:latest -f")
		sh ("docker image prune -f")
		sh ("docker container prune -f")
	}
}
