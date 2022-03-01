node {
    checkout scm

    #if blank uses dockerhub
    docker.withRegistry('https://registry.example.com') {

        docker.image('my-custom-image').inside {
            sh 'make test'
        }
    }
}
