# Team
### Roles (-> who does what?)
- View: (Ugur Dogan, Jun-Wei To and Aditya Bharadwaj)
- Model: (Ugur Dogan Jun-Wei To and Aditya Bharadwaj)
- Network / Controller: (Yuanyuan Qu and Yili Li)
- Integration: (Yuanyuan Qu, Yili Li and Aditya Bharadwaj)

### Communication (-> explain how everyone in the team needs to communicate)
- Pair Programming
- Communicate early, on discord/Whatsapp so everyone can see it and contribute.
- inform the team what you are working on, ask for help early


# Project

### Setup (-> explain how everyone needs to set up their IDE/project when working on it)
- Java (21.0.3)
- Gradle
- IntelliJ 
- Plugins: (JavaDoc, JUnit)

### Meetings (-> explain what everyone needs to before/ in/ after/ the meetings, when are meetings?)
- Weekly meeting with tutor (Francis) on wednesday 16 - 17 
- Before meeting: Prepare standup, upload weekly report, think of questions/ discussion topics and problems you're facing currently.
- During meeting: Standup (done, planned, good vs. bad, feedback), Discussion, Aditya writes a protocol of the meeting (+ concrete next steps)
- After meeting: Upload protocol, (optional) meeting without tutor and discussions and suggestions about the code and the shippable product.
- Meetings during the weeks at-least once (on discord/ in person)


### Git (-> explain how everyone need to work with gitlab, i.e. explain the workflow, what is allowed, what is forbidden)
- Let's keep the workflow simple: Everybody works on different issues/features, create a draft branch for it and code locally and push it as soon as the feature/issue is ready/resolved and merge the branch to the development branch.
- Before testing/checking the IDE, please make sure that you do: 
  > Read the CHANGELOG

  > git fetch

  > git checkout 

  > git pull origin
  
- before merging the branches, make sure you ask for approvals.
- At the End, Merge the product to Main.




### Testing (-> Explain how everyone need to test, what needs to be tested manually, test reports?, unit-tests?, what needs to be tested?)
- GUI needs to be tested manually, as much as possible 
- Unit-Tests for the Model and network part


### Code Review (-> How does the code review work?, via gitlab?, manually?, where is feedback communicated?, etc.)
- Code Review shall be communicated through GitLab and Manually on discord. 


### Definition of Done (-> Explain what criteria the code needs to fulfill before committing/ merging)
- Before committing: JavaDoc for each method
- Before committing: Comments where needed
- Before committing: Adheres to google java style guide (-> checkstyle, spot bugs, ...)
- Before committing: Maximum commit size ....?
- Before merging: Code review (by another team member)
- Before merging: No unit tests fail, at least manually tested, no game breaking bugs.
