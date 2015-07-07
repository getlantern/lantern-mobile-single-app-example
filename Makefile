APK_FILE := ./app/build/outputs/apk/app-debug.apk

define pkg_variables
	$(eval PACKAGE := $(shell aapt dump badging $(APK_FILE)|awk -F" " '/package/ {print $$2}'|awk -F"'" '/name=/ {print $$2}'))
	$(eval MAIN_ACTIVITY := $(shell aapt dump badging $(APK_FILE)|awk -F" " '/launchable-activity/ {print $$2}'|awk -F"'" '/name=/ {print $$2}' | grep MainActivity))
endef

.PHONY: all

all: build-debug install run

build-debug:
	./gradlew assembleDebug

$(APK_FILE): build-debug

install: $(APK_FILE)
	$(call pkg_variables)
	adb install -r $(APK_FILE)

uninstall:
	$(call pkg_variables)
	adb uninstall $(PACKAGE)

run:
	$(call pkg_variables)
	adb shell am start -n $(PACKAGE)/$(MAIN_ACTIVITY)

clean:
	./gradlew clean
