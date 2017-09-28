import com.pipeline.simple.SimplePipelineBuilder

def projectProperties = [
        projectName: 'project-echo',
        repository: 'https://github.com/dev-trainings/echo-gradle-project.git',
        scmPoll: '@midnight'
]

new SimplePipelineBuilder(projectProperties).build(this)

