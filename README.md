# Mobile Week 4

Week 4 lecture of Mobile Applications course

## What was covered

1. Activate view binding for easier references
    - Inflate the view binder
    - Set the content view as root of binder
2. New intents
    - Implicit URL browse intent
    - Implicit map view intent
    - Implicit phone call intent
        - Requires user permissions; modify manifest
        - Check whether permission was granted; automatically call if so
        - Alert user how to enable permission if denied initially
    - Implicit image picker intent
3. Activity Launchers
    - Register launchers
    - Launch launchers to use
    - Can be used with implicit events for async results, and with explicit intents to return results
