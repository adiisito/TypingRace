# Team13 : SpaceRally
### Roles
- View: (_Ugur Dogan, Jun-Wei To and Aditya Bharadwaj_)
- Model: (_Ugur Dogan Jun-Wei To and Aditya Bharadwaj_)
- Network / Controller: (_Yuanyuan Qu and Yili Li_)
- Integration: (_Yuanyuan Qu, Yili Li and Aditya Bharadwaj_)
- Protocol & Team-Policy: (_Aditya Bharadwaj_)

### Communication 
- Pair Programming (essentially write the code together and ask for reviews!)
- Communicate early, on discord/Whatsapp so everyone can see it and contribute.
- Inform the team what you are working on, ask for help early
- Always cooperate with team members regarding the merge requests,
  so that there are as less merge conflicts as possible.
- Discord/offline meetings/talks; at least once a week.
  (apart from the weekly meet with the tutor on wednesdays.)


# Project

### Setup 
Java Version: **Java 17 or higher.**

Build Tool: **Gradle 6.4 or higher.**

IDE: **IntelliJ IDEA** shall be used for the group project.

### Required Plugins

JUnit

JavaDoc

CheckStyle

Meetings

### Meetings 

#### Weekly Meeting

Time: Wednesday 16:00 - 17:00

#### Before Meeting:

Prepare your standup update.
Upload the weekly report.
Think of questions, discussion topics, and problems you're currently facing.

#### During Meeting:

Conduct a standup (covering what was done, what is planned, positives and negatives,
and feedback).
Engage in discussions.
Aditya writes a protocol of the meeting, including concrete next steps.

#### After Meeting:

Upload the protocol.
Optional follow-up meeting without the tutor for additional discussions and suggestions about 
the code and the shippable product.
Manual testing in person and exchange ideas about new features and fixes.

#### Optional Meetings

Schedule additional meetings during the week on Discord or in person at least once for further
coordination.


### Git 
-For a smooth and efficient development process, we will follow a structured and 
straightforward Git workflow. The steps and best practices for working with GitLab,
detailing what is allowed and what is forbidden.

#### Workflow

Issue/Feature Assignment:

Each team member works on different issues or features to avoid overlap and ensure 
parallel progress.
Creating a Draft Branch:

For each assigned issue or feature, create a new draft branch from the development branch.
Name the branch descriptively, ideally including the issue number and a brief description 
of the feature (e.g., feature/123-add-login).

Local Development:

Code locally on your draft branch.
Regularly commit your changes with clear and descriptive commit messages to document your 
progress and changes.

Pushing Changes:

Once the feature or issue is resolved and tested locally, push your draft branch to the remote
repository on GitLab.

Read the CHANGELOG:

Before testing or checking changes in the IDE, always read the CHANGELOG to stay informed about recent updates and modifications made by other team members.
Synchronizing with Remote Repository:

Fetch the latest changes from the remote repository:

git fetch

Checkout to the development branch to mak sure are working with the latest codebase:


git checkout (development)

Pull the latest changes from the remote development branch:

git pull origin (development)

Merging Draft Branch:

Before merging your draft branch into the development branch, seek approvals from at least one other team member. Create a merge request (MR) on GitLab for your draft branch into the development branch.
Address any feedback received during the code review process and make necessary changes. 
Final Integration:

After approvals and successful code review, merge your draft branch into the development branch.
Once the development branch is thoroughly tested and deemed stable, it can be merged into the main branch for a final product release.

Best Practices

Commit Messages:

Write clear, concise, and descriptive commit messages.
Follow the format: [ISSUE-NUMBER] Brief description of changes.

Branch Naming:

Use a consistent and descriptive naming convention for branches (e.g., feature/123-car-progress).

Merge Requests:

Always create a merge request for your draft branch into the development branch.
Provide a detailed description of the changes in the merge request.

Code Review:

Engage actively in code reviews. Provide constructive feedback and be open to receiving feedback.

Forbidden Practices

Direct Commits to Main or Development:

Never commit directly to the main or development branches. Always work on a draft branch and use merge requests.

Ignoring Code Reviews: !!!

Do not merge branches without seeking approvals and conducting code reviews.
Inadequate Documentation:

Avoid insufficient commit messages and merge request descriptions. Proper documentation is very importan to maintain a clear project history.




### Testing 
Manual Testing for GUI:

- The Graphical User Interface (GUI) should be tested manually.
- Test all user interactions, including buttons, forms, navigation, and any dynamic elements.
- Ensure the GUI is user-friendly, responsive, and free of visual glitches.
- Do usability testing to gather feedback on the user experience.
- Unit Testing for Model and Network:

Write unit tests for the Model and Network components of the project.
Ensure all core functionalities are covered by unit tests.
Test edge cases and handle exceptions to prevent unexpected behavior.
Aim for high code coverage to ensure that most of the codebase is tested.

Testing Process

Manual Testing:

Run GUI to identify any issues that automated tests might miss.
Use test cases and scenarios to systematically verify all features and functionalities.

Unit Testing:

Develop unit tests alongside feature implementation to ensure immediate validation.
Use JUnit framework to create and manage unit tests.
Run unit tests frequently to catch issues early in the development cycle.

What Needs to be Tested

GUI:

Visual layout and design.
User interactions
Input validation and error handling.
Responsiveness and compatibility across different devices and screen sizes.

Network communication and data transfer.
Error handling and recovery mechanisms.
Performance under different network conditions.


### Code Review

Submitting Code for Review:

When a feature or issue is resolved and the code is ready, push your changes to a draft branch on GitLab.
Create a Merge Request (MR) from your draft branch to the development branch.
Provide a detailed description of the changes, including any relevant context or dependencies.

Reviewing Code:

Team members are notified of new MRs via GitLab and the designated Discord channel.
Reviewers should thoroughly examine the code, checking for:
Adherence to coding standards and best practices.
Completeness and accuracy of the implementation.
Potential bugs or issues.
Performance and efficiency.
Adequate testing and test coverage.
Test the functionality locally if necessary to verify behavior.

Providing Feedback:

Feedback is communicated through GitLab comments directly within the MR.
For more in-depth discussions or clarifications, use the designated Discord channel.
Provide constructive and specific feedback, highlighting both strengths and areas for improvement.

Addressing Feedback:

The author of the MR should promptly address feedback and make necessary changes.
Push the updated code to the same draft branch.
Respond to comments on GitLab, indicating how the feedback was addressed.



### Definition of Done 

Criteria Before Committing

JavaDoc for Each Method:

Every method must have a complete JavaDoc comment that describes its purpose, parameters, and return values.
Ensure the JavaDoc is clear, concise, and informative to help in understanding the code.

Comments Where Needed:

Add comments in the code where necessary to explain complex logic or decisions.
Ensure comments are helpful and do not clutter the code with unnecessary information.

Stick to Google Java Style Guide:

Follow the Google Java Style Guide for all code formatting and style.
Use tools Checkstyle to enforce these standards.
Ensure that the code is clean, readable, and consistently formatted.

Maximum Commit Size:

Keep commit sizes manageable and focused on specific changes or features.
Avoid large, monolithic commits that are difficult to review. Aim for atomic commits that address one issue or feature at a time.

Criteria Before Merging

Passing Unit Tests:

Ensure that all unit tests pass without any failures.

Manual Testing:

Conduct manual testing to verify that the code works as intended in real-world scenarios.
Ensure that the functionality is thoroughly tested and any identified issues are resolved.

No Game-Breaking Bugs:

Confirm that there are no critical or game-breaking bugs in the code.
